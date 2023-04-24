import { TailSpin } from 'react-loader-spinner';

const Loading = () => {
  return <TailSpin
    height="80"
    width="80"
    color="#FFFFFF"
    ariaLabel="tail-spin-loading"
    radius="1"
    visible={true}
  />
}

export default Loading