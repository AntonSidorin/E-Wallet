import CollapsibleAction from 'component/wallet/action/CollapsibleAction';
import TopupWallet from 'component/wallet/action/TopupWallet';
import WithdrawWallet from 'component/wallet/action/WithdrawWallet';
import Transfer from 'component/wallet/action/Transfer';

const WalletActions = ({walletId}) => {

  return (
    <div className="walletActions">
      <CollapsibleAction label="Topup">
        <TopupWallet walletId={walletId} />
      </CollapsibleAction>
      <CollapsibleAction label="Withdraw">
        <WithdrawWallet walletId={walletId} />
      </CollapsibleAction>
      <CollapsibleAction label="Transfer">
        <Transfer walletId={walletId} />
      </CollapsibleAction>
    </div>
  );
};

export default WalletActions;