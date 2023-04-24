import { useNavigate } from 'react-router-dom';

const WalletWidget = ({wallet}) => {

  const navigate = useNavigate();

  return (
      <div onClick={() => navigate(`/wallet/${wallet.id}`)} className="walletWidget" key={wallet.id}>
        <div title="Wallet balance" className="header">{(Math.round(wallet.balance * 100) / 100).toFixed(2)}</div>
        <span>{wallet.name}</span>
        <div title="Copy wallet id to clipboard" className="copyId" onClick={(e) => {e.stopPropagation(); navigator.clipboard.writeText(wallet.id)}}>id</div>
      </div>
  );
};

export default WalletWidget;