import { useNavigate } from 'react-router-dom';

const CreateWalletWidget = () => {

  const navigate = useNavigate();

  return (
    <div onClick={() => navigate('/createWallet')} className="walletWidget walletActionWidget">
      <span>Create Wallet</span>
    </div>
  );
};

export default CreateWalletWidget;