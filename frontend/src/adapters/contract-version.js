import { adaptScaffoldV1, serializeScaffoldV1 } from './v1/scaffold';
import { serializeScaffoldSummaryByMetaMaskV1, serializeScaffoldSummaryByApiV1 } from './v1/scaffold-summary';

const versionMap = {
  V1: {
    version: () => 'V1',
    adaptScaffold: adaptScaffoldV1,
    serializeScaffold: serializeScaffoldV1,
    serializeScaffoldSummaryByMetaMask: serializeScaffoldSummaryByMetaMaskV1,
    serializeScaffoldSummaryByApi: serializeScaffoldSummaryByApiV1
  }
  // V2: {
  //   version: () => 'V2'
  // }
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
