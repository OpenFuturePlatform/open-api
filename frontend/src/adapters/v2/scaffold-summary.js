import { serializeScaffoldSummaryByMetaMaskV1 } from '../v1/scaffold-summary';

export const serializeScaffoldSummaryByMetaMaskV2 = source => {
  const summaryV1 = serializeScaffoldSummaryByMetaMaskV1(source);
  const { 6: activated } = source;
  return {
    ...summaryV1,
    activated
  };
};
