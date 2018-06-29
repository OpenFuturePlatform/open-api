export const parseApiError = e => {
  const status = e.response.status;
  const message = e.response.data.message || e.response.statusText;
  return `${status}: ${message}`;
};
