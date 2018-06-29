import * as moment from 'moment';
import { DD_MMM_YYYY } from '../const/time-formats';

export const formatDate = (value, format) => (value ? moment(value).format(format || DD_MMM_YYYY) : '-');
