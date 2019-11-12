import {getFromBN} from '../../utils/getFromBN';
import {MIN_CONTRACT_DEPOSIT} from '../../const';

export const serializeEthereumScaffoldSummaryByMetaMaskV1 = source => {
  const {
    0: fiatAmount,
    1: currency,
    2: conversionAmount,
    3: transactionIndex,
    4: developerAddress,
    5: tokenBalance
  } = source;
  const activated = tokenBalance >= MIN_CONTRACT_DEPOSIT;
  return {
    fiatAmount,
    currency,
    conversionAmount: getFromBN(conversionAmount),
    transactionIndex: getFromBN(transactionIndex),
    developerAddress,
    tokenBalance: getFromBN(tokenBalance) / 100000000,
    activated
  };
};

export const serializeEthereumScaffoldSummaryByApiV1 = rawSummary => {
  const tokenBalance = rawSummary.tokenBalance / 100000000;
  const activated = rawSummary.enabled;
  return { ...rawSummary, activated, tokenBalance };
};
