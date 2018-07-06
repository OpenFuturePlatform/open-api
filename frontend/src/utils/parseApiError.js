import { groupBy, mapValues } from 'lodash';

export const parseApiError = e => {
  const status = e.response.status;
  const message = e.response.data.message || e.response.statusText;
  const groupedErrors = groupBy(e.response.data.errors, 'field');
  const errorList = mapValues(groupedErrors, it => it.map(error => error.code));
  return { message: `${status}: ${message}`, errors: errorList };
};
