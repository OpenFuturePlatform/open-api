import {getScaffoldSetSelector} from './getEthereumScaffoldSet';

export const getShareHoldersSelector = (state, scaffoldAddress) => {
  const scaffoldSet = getScaffoldSetSelector(state, scaffoldAddress);
  return scaffoldSet.shareHolders || [];
};
