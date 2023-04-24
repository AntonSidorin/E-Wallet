import { useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Loading from 'component/util/Loading';
import { useCreateWalletMutation } from 'api/wallet/walletApiSlice';

const CreateWallet = () => {
  const nameRef = useRef();
  const errRef = useRef();
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [balance, setBalance] = useState('');
  const [errMsg, setErrMsg] = useState('');
  const navigate = useNavigate();
  const [createWallet, { isLoading }] = useCreateWalletMutation();

  useEffect(() => {
    nameRef.current.focus();
  }, []);

  useEffect(() => {
    setErrMsg('');
  }, [name, description, balance]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await createWallet({ name, description, balance }).unwrap();
      setName('');
      setDescription('');
      setBalance('');
      navigate('/dashboard');
    } catch (error) {
      if (!error?.status) {
        // isLoading: true until timeout occurs
        setErrMsg('No Server Response');
      } else if (error?.data?.messages) {
        setErrMsg(error.data.messages);
      } else if (error.status === 400) {
        setErrMsg('Missing Wallet Name Or Description Or Balance');
      } else if (error.status === 401) {
        setErrMsg('Unauthorized');
      } else {
        setErrMsg('Wallet Creation Failed');
      }
      errRef.current.focus();
    }
  };

  const handleNameInput = (e) => setName(e.target.value);
  const handleDescriptionInput = (e) => setDescription(e.target.value);
  const handleBalanceInput = (e) => {
    const balance = e.target.value;
    const validated = balance.match(/^(\d*\.?\d{0,2}$)/);
    if (validated) {
      setBalance(balance);
    }
  };

  if (isLoading) return <Loading />;

  return (
    <section className="createWallet">
      <p ref={errRef} className={errMsg ? 'errmsg' : 'offscreen'} aria-live="assertive">{errMsg}</p>

      <h1>Create New Wallet</h1>

      <form onSubmit={handleSubmit}>
        <label htmlFor="name">Name:</label>
        <input
          type="text"
          id="Name"
          maxLength="50"
          placeholder="Wallet name (50 symbols)"
          ref={nameRef}
          value={name}
          onChange={handleNameInput}
          autoComplete="off"
          required
        />

        <label htmlFor="balance">Balance:</label>
        <input
          type="number"
          min="0"
          max="10000"
          step="0.01"
          id="balance"
          placeholder="0.00"
          onChange={handleBalanceInput}
          value={balance}
          autoComplete="off"
          required
        />

        <label htmlFor="description">Description:</label>
        <textarea
          id="description"
          maxLength="250"
          onChange={handleDescriptionInput}
          value={description}
          placeholder="Wallet description (250 symbols)"
          autoComplete="off"
        />

        <button>Create</button>
      </form>
      <div className="back">
        <Link to="/dashboard">Back</Link>
      </div>
    </section>
  );

};

export default CreateWallet;