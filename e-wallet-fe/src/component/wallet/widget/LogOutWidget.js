import { useDispatch } from 'react-redux';
import { logOut } from 'store/auth/authSlice';

const LogOutWidget = () => {

  const dispatch = useDispatch();

  return (
    <div onClick={() => {dispatch(logOut());}} className="walletWidget walletActionWidget">
      <span>Log Out</span>
    </div>
  );
};

export default LogOutWidget;