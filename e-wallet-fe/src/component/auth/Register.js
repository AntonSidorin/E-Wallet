import { useRef, useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

import { useDispatch } from 'react-redux';
import { setCredentials } from 'store/auth/authSlice';
import { useRegisterMutation } from 'api/auth/authApiSlice';
import Loading from 'component/util/Loading';

const Register = () => {
  const usernameRef = useRef();
  const firstnameRef = useRef();
  const lastnameRef = useRef();

  const [username, setUsername] = useState('');
  const [firstname, setFirstname] = useState('');
  const [lastname, setLastname] = useState('');
  const [password, setPassword] = useState('');
  const [errMsg, setErrMsg] = useState('');

  const navigate = useNavigate();

  const [register, { isLoading }] = useRegisterMutation();
  const dispatch = useDispatch();

  useEffect(() => {
    usernameRef.current.focus();
  }, []);

  useEffect(() => {
    setErrMsg('');
  }, [usernameRef, password]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const userData = await register({ username, password, firstname, lastname }).unwrap();
      dispatch(setCredentials({ ...userData, username }));
      setUsername('');
      setPassword('');
      setFirstname('');
      setLastname('');
      navigate('/dashboard');
    } catch (error) {
      if (!error?.status) {
        // isLoading: true until timeout occurs
        setErrMsg('No Server Response');
      } else if (error?.data?.messages) {
        setErrMsg(error?.data?.messages);
      } else if (error.status === 400) {
        setErrMsg('Missing Username or Password');
      } else if (error.status === 401) {
        setErrMsg('Unauthorized');
      } else {
        setErrMsg('Register Failed');
      }
    }
  };

  const handleUsernameInput = (e) => setUsername(e.target.value);
  const handlePasswordInput = (e) => setPassword(e.target.value);
  const handleFirstnameInput = (e) => setFirstname(e.target.value);
  const handleLastnameInput = (e) => setLastname(e.target.value);

  if (isLoading) return <Loading />;

  return (
    <section className="register">

      <p className={errMsg ? 'errmsg' : 'offscreen'} aria-live="assertive">{errMsg}</p>

      <h1>Register</h1>

      <form onSubmit={handleSubmit}>
        <label htmlFor="username">Username:</label>
        <input
          type="text"
          id="username"
          maxLength="50"
          ref={usernameRef}
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

        <label htmlFor="firstname">Firstname:</label>
        <input
          type="text"
          id="firstname"
          maxLength="50"
          ref={firstnameRef}
          value={firstname}
          onChange={handleFirstnameInput}
          autoComplete="off"
          required
        />

        <label htmlFor="lastname">Lastname:</label>
        <input
          type="text"
          id="lastname"
          maxLength="50"
          ref={lastnameRef}
          value={lastname}
          onChange={handleLastnameInput}
          autoComplete="off"
          required
        />

        <button>Register</button>
      </form>
      <div className="back">
        <Link to="/">Back</Link>
      </div>
    </section>
  );

};
export default Register;