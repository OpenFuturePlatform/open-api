import {combineReducers} from 'redux';
import {reducer as reduxForm} from 'redux-form';
import authReducer from './authReducer';
import currencyConversionReducer from './currencyConversionReducer';
import currentScaffoldInstanceReducer from './currentScaffoldInstanceReducer';
import modalReducer from './modalReducer';
import onchainScaffoldSummaryReducer from './onchainScaffoldSummaryReducer';
import scaffoldFeildsReducer from './scaffoldFeildsReducer';
import scaffoldsReducer from './scaffoldsReducer';
import withdrawModalReducer from './withdrawModalReducer';
import ethAccountReducer from './ethAccountReducer';

export default combineReducers({
  form: reduxForm,
  auth: authReducer,
  currencyConversionValue: currencyConversionReducer,
  currentScaffoldInstance: currentScaffoldInstanceReducer,
  modalInfo: modalReducer,
  onchainScaffoldSummary: onchainScaffoldSummaryReducer,
  scaffoldFeilds: scaffoldFeildsReducer,
  scaffolds: scaffoldsReducer,
  withdrawModalInfo: withdrawModalReducer,
  ethAccount: ethAccountReducer
});
