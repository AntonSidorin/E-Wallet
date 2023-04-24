import { Link } from 'react-router-dom';

const Error = ({ to, name, message }) => {
  return (
    <section className="error">
      <p className="errmsg" aria-live="assertive">
        <span>An unexpected error has occurred. {message}</span>
        {to ? <Link to="{to}">{name? name : to}</Link> : null}
      </p>
    </section>
  );
};

export default Error;

