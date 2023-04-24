import { useContext, useEffect, useRef, useState } from 'react';
import { useWithdrawWalletMutation } from 'api/wallet/walletApiSlice';
import Loading from 'component/util/Loading';
import { CollapsibleActionContext } from './CollapsibleAction';

const WithdrawWallet = ({ walletId }) => {

  const setErrMsg = useContext(CollapsibleActionContext);

  const amountRef = useRef();
  const [amount, setAmount] = useState('');
  const [withdrawWallet, { isLoading }] = useWithdrawWalletMutation();

  useEffect(() => {
    amountRef.current.focus();
  }, []);

  useEffect(() => {
    setErrMsg('');
  }, [amount, setErrMsg]);

  const handleSubmit = async (e) => {

    e.preventDefault();

    if (amount <= 0) {
      return;
    }

    try {
      await withdrawWallet({ walletId, amount }).unwrap();
      amountRef.current.focus();
    } catch (error) {
      if (!error?.status) {
        // isLoading: true until timeout occurs
        setErrMsg('No Server Response');
      } else if (error?.data?.messages) {
        setErrMsg(error.data.messages);
      } else if (error.status === 400) {
        setErrMsg('Missing Wallet Amount');
      } else if (error.status === 401) {
        setErrMsg('Unauthorized');
      } else {
        setErrMsg('Wallet Withdraw Failed');
      }
    }
  };

  const handleAmountInput = (e) => {
    const amount = e.target.value;
    const validated = amount.match(/^(\d*\.?\d{0,2}$)/);
    if (validated) {
      setAmount(amount);
    }
  };

  if (isLoading) return <Loading />;

  return (
    <div id="withdraw">
      <form onSubmit={handleSubmit}>
        <label htmlFor="amount">Amount:</label>
        <input
          ref={amountRef}
          type="number"
          min="0"
          max="10000"
          step="0.01"
          id="amount"
          placeholder="0.00"
          onChange={handleAmountInput}
          value={amount}
          autoComplete="off"
          required
        />
        <button>Withdraw</button>
      </form>
    </div>
  );
};

export default WithdrawWallet;