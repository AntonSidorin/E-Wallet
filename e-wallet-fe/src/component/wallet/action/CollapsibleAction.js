import { createContext, useRef, useState } from 'react';
import DisplayErrorMessages from '../error/DisplayErrorMessages';

export const CollapsibleActionContext = createContext(undefined);

const CollapsibleAction = ({ label, children }) => {

  const [open, setOpen] = useState(false);

  const [errMsg, setErrMsg] = useState('');

  const contentRef = useRef();

  const toggle = () => {
    setOpen(!open);
  };

  return (
    <div className="collapsible-parent">
      <button onClick={toggle} className="collapsible">{label}</button>
      {open && (
        <div ref={contentRef} className="content-parent">
          <div className="content">
            <DisplayErrorMessages messages={errMsg} />
            <CollapsibleActionContext.Provider value={setErrMsg}>
              {children}
            </CollapsibleActionContext.Provider>
          </div>
        </div>
      )}
    </div>

  );
};

export default CollapsibleAction;