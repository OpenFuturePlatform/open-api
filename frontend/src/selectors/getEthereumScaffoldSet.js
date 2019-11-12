export const getScaffoldSetSelector = ({ scaffoldsById }, scaffoldAddress) => {
  return scaffoldsById[scaffoldAddress] || {};
};
