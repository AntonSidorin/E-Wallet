import { useRef, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useDispatch } from 'react-redux';
import { setCredentials } from 'store/auth/authSlice';
import { useLoginMutation } from 'api/auth/authApiSlice';
import Loading from 'component/util/Loading';

const Login = () => {
  const userRef = useRef();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errMsg, setErrMsg] = useState('');
  const navigate = useNavigate();

  const [login, { isLoading }] = useLoginMutation();
  const dispatch = useDispatch();

  useEffect(() => {
    userRef.current.focus();
  }, []);

  useEffect(() => {
    setErrMsg('');
  }, [username, password]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const userData = await login({ username, password }).unwrap();
      dispatch(setCredentials({ ...userData, username }));
      setUsername('');
      setPassword('');
      navigate('/dashboard');
    } catch (error) {
      if (!error?.status) {
        // isLoading: true until timeout occurs
        setErrMsg('No Server Response');
      }else if (error?.data?.message) {
        setErrMsg(error?.message);
      } else if (error.status === 500) {
        setErrMsg('Internal Server Error');
      } else if (error.status === 400) {
        setErrMsg('Missing Username or Password');
      } else if (error.status === 401) {
        setErrMsg('Unauthorized');
      } else {
        setErrMsg('Login Failed');
      }
    }
  };

  const handleUsernameInput = (e) => setUsername(e.target.value);

  const handlePasswordInput = (e) => setPassword(e.target.value);

  if (isLoading) return <Loading />;

  return (
    <section className="login">

      <p className={errMsg ? 'errmsg' : 'offscreen'} aria-live="assertive">{errMsg}</p>

      <form onSubmit={handleSubmit}>
        <label htmlFor="username">Username:</label>
        <input
          type="text"
          id="username"
          maxLength="50"
          ref={userRef}
          value={username}
          onChange={handleUsernameInput}
          autoComplete="on"
          required
        />

        <label htmlFor="password">Password:</label>
        <input
          type="password"
          id="password"
          maxLength="50"
          onChange={handlePasswordInput}
          value={password}
          autoComplete="on"
          required
        />
        <button>Sign In</button>
      </form>
    </section>
  );
};
export default Login;