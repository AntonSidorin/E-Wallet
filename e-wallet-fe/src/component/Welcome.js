import { Link } from 'react-router-dom';
import Login from 'component/auth/Login';

const Welcome = () => {
  return (
    <section className="welcome">
      <header>
        <h1>Welcome to Wallet Manager!</h1>
      </header>
      <main>
        <div className="info">
          Wallet Manager can help you to keep track of your income and expenses with quality info and keep track of
          your daily, monthly and yearly finances.
        </div>
        <Login />
      </main>
      <footer>
        <span className="right"><Link to="/register">Register</Link></span>
      </footer>
    </section>
  );
};

export default Welcome;
