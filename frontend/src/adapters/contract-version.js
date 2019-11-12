import {adaptEthereumScaffoldV1, serializeEthereumScaffoldV1} from './v1/ethereum-scaffold';
import {
  serializeEthereumScaffoldSummaryByApiV1,
  serializeEthereumScaffoldSummaryByMetaMaskV1
} from './v1/ethereum-scaffold-summary';
import {serializeEthereumScaffoldSummaryByMetaMaskV2} from './v2/ethereum-scaffold-summary';

const versionMap = {
  V1: {
    version: () => 'V1',
    adaptScaffold: adaptEthereumScaffoldV1,
    serializeScaffold: serializeEthereumScaffoldV1,
    serializeScaffoldSummaryByMetaMask: serializeEthereumScaffoldSummaryByMetaMaskV1,
    serializeScaffoldSummaryByApi: serializeEthereumScaffoldSummaryByApiV1
  },
  V2: {
    version: () => 'V2',
    serializeScaffoldSummaryByMetaMask: serializeEthereumScaffoldSummaryByMetaMaskV2
  }
};

export const contractVersion = initVersion => {
  let version = initVersion;
  if (!version) {
    console.error('>> API version is not set');
  }
  if (version === 'V1') {
    return versionMap[version];
  }
  if (version === 'latest') {
    const versionNumbers = Object.keys(versionMap).map(key => key.slice(1));
    const maxVersion = Math.max(...versionNumbers);
    version = `V${maxVersion}`;
  }
  return { ...versionMap['V1'], ...versionMap[version] };
};
