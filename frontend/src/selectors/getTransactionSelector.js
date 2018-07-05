import { getScaffoldSetSelector } from './getScaffoldSet';

export const getTransactionSelector = (state, scaffoldAddress) => {
  const scaffoldSet = getScaffoldSetSelector(state, scaffoldAddress);
  return scaffoldSet.transactions || { totalCount: 0, list: [] };
};
