import { groupBy, mapValues } from 'lodash';
import { adaptScaffold } from './scaffold-adapter';

export const parseApiError = e => {
  const status = e.response.status;
  const message = e.response.data.message || e.response.statusText;
  const groupedErrors = groupBy(e.response.data.errors, 'field');
  const adaptedErrors = adaptScaffold(groupedErrors);
  const errorList = mapValues(adaptedErrors, it => it.map(error => error.code));
  return { message: `${status}: ${message}`, errors: errorList };
};
