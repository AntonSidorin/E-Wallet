import WalletWidget from './WalletWidget';

const WalletWidgets = ({wallets}) => {
  return wallets?.length > 0 ? wallets.map(wallet => {
    return <WalletWidget key={wallet.id} wallet={wallet} />;
  }) : <div className="no-wallets">You don't have any wallets yet.</div>;
};

export default WalletWidgets;