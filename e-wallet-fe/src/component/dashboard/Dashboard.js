import { selectCurrentUser } from 'store/auth/authSlice';
import { useGetWalletsQuery } from 'api/wallet/walletApiSlice';
import Loading from 'component/util/Loading';
import CreateWalletWidget from 'component/wallet/widget/CreateWalletWidget';
import LogOutWidget from 'component/wallet/widget/LogOutWidget';
import WalletWidgets from 'component/wallet/widget/WalletWidgets';
import Error from '../error/Error';
import RefreshDashboardWidget from '../wallet/widget/RefreshDashboardWidget';
import { useSelector } from 'react-redux';

const Dashboard = () => {

  const user = useSelector(selectCurrentUser);

  const welcome = user ? `Welcome ${user.firstname} ${user.lastname}` : 'Welcome';

  const {
    data,
    refetch,
    isLoading,
    isSuccess,
  } = useGetWalletsQuery();

  if (isLoading) return <Loading />;

  return (
    <section className="dashboard">
      <section className="info">
        <h1>{welcome}</h1>
      </section>
      <section className="wallets">
        <div className="walletActionWidgets">
          <CreateWalletWidget />
          <RefreshDashboardWidget refetch={refetch} />
          <LogOutWidget />
        </div>
        {isSuccess ? <WalletWidgets wallets={data} /> : <Error message="Please try to refresh." />}
      </section>
    </section>
  );
};

export default Dashboard;