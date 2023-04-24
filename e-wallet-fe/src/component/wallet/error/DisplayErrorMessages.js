const DisplayErrorMessages = ({ messages }) => {

  const isArray = Array.isArray(messages);

  if (!messages) {
    return null;
  } else {
    return (
      <div className={messages ? 'errmsg' : 'offscreen'} aria-live="assertive">
        {isArray ? <ul>{messages.map((message) => <li key={message}>{message}</li>)}</ul> :
          <ul>
            <li>{messages}</li>
          </ul>
        }
      </div>
    );
  }

};

export default DisplayErrorMessages;