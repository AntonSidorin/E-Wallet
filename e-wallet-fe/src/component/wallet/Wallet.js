import WalletInformation from 'component/wallet/information/WalletInformation';
import WalletTransactions from 'component/wallet/transaction/WalletTransactions';
import WalletActions from 'component/wallet/action/WalletActions';
import { Link, useParams } from 'react-router-dom';
import CollapsibleAction from 'component/wallet/action/CollapsibleAction';
import { useGetWalletQuery } from 'api/wallet/walletApiSlice';
import Loading from 'component/util/Loading';
import Error from 'component/error/Error';

const Wallet = () => {

  const { walletId } = useParams();

  const {
    data: wallet,
    isLoading,
    isError,
  } = useGetWalletQuery(walletId);

  if (isLoading) return <Loading />;

  if (isError) return (
    <Error to="/dashboard" name="dashboard" message="Please try to open wallet from " />
  );

  return (
    <section>
      <section>
        <Link to="/dashboard">Dashboard</Link>
      </section>
      <WalletInformation wallet={wallet} />
      <WalletActions walletId={walletId} />
      <CollapsibleAction label="Transactions">
        <WalletTransactions walletId={walletId} />
      </CollapsibleAction>
    </section>
  );

};

export default Wallet;