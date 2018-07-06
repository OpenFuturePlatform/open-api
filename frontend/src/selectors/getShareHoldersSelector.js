import { getScaffoldSetSelector } from './getScaffoldSet';

export const getShareHoldersSelector = (state, scaffoldAddress) => {
  const scaffoldSet = getScaffoldSetSelector(state, scaffoldAddress);
  return scaffoldSet.shareHolders || [];
};
