import { useContext, useEffect, useRef, useState } from 'react';
import { CollapsibleActionContext } from 'component/wallet/action/CollapsibleAction';
import { useTransferMutation } from 'api/wallet/walletApiSlice';
import Loading from 'component//util/Loading';

const Transfer = ({walletId}) => {

  const setErrMsg = useContext(CollapsibleActionContext);

  const transferWalletIdRef = useRef();
  const amountRef = useRef();

  const [amount, setAmount] = useState('');
  const [transferWalletId, setTransferWalletId] = useState('');
  const [transfer, { isLoading }] = useTransferMutation();

  useEffect(() => {
    transferWalletIdRef.current.focus();
  }, []);

  useEffect(() => {
    setErrMsg('');
  }, [amount, setErrMsg]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (amount <= 0) {
      return;
    }

    if (walletId === transferWalletId) {
      setErrMsg('To Wallet Id should be different to the current Wallet Id.');
      return;
    }

    try {
      await transfer({ walletId, transferWalletId, amount }).unwrap();
      amountRef.current.focus();
    } catch (error) {
      if (!error?.status) {
        // isLoading: true until timeout occurs
        setErrMsg(['No Server Response']);
      } else if (error?.data?.messages) {
        setErrMsg(error.data.messages);
      } else if (error.status === 400) {
        setErrMsg(['Missing Wallet Amount OR Transfer To Wallet Id']);
      } else if (error.status === 401) {
        setErrMsg(['Unauthorized']);
      } else {
        setErrMsg('Wallet Transfer Failed');
      }
    }
  };

  const handleTransferWalletIdInput = (e) => {
    setTransferWalletId(e.target.value);
  };

  const handleTransferAmountInput = (e) => {
    const amount = e.target.value;
    const validated = amount.match(/^(\d*\.?\d{0,2}$)/);
    if (validated) {
      setAmount(amount);
    }
  };

  if (isLoading) return <Loading />;

  return (
    <div id="transfer">
      <form onSubmit={handleSubmit}>
        <label htmlFor="transfer_wallet">To Wallet Id:</label>
        <input
          ref={transferWalletIdRef}
          type="text"
          id="transfer_wallet"
          placeholder="Wallet Id to transfer amount"
          onChange={handleTransferWalletIdInput}
          value={transferWalletId}
          autoComplete="off"
          required
        />
        <label htmlFor="transfer_amount">Amount:</label>
        <input
          ref={amountRef}
          type="number"
          min="0"
          max="10000"
          step="0.01"
          id="transfer_amount"
          placeholder="0.00"
          onChange={handleTransferAmountInput}
          value={amount}
          autoComplete="off"
          required
        />
        <button>Transfer</button>
      </form>
    </div>
  );
};

export default Transfer;