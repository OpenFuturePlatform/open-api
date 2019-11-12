import {serializeEthereumScaffoldSummaryByMetaMaskV1} from '../v1/ethereum-scaffold-summary';

export const serializeEthereumScaffoldSummaryByMetaMaskV2 = source => {
  const summaryV1 = serializeEthereumScaffoldSummaryByMetaMaskV1(source);
  const { 6: activated } = source;
  return {
    ...summaryV1,
    activated
  };
};
