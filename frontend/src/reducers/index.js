import {combineReducers} from 'redux';
import {reducer as reduxForm} from 'redux-form';
import authReducer from './authReducer';
import currencyConversionReducer from './currencyConversionReducer';
import currentScaffoldInstanceReducer from './currentScaffoldInstanceReducer';
import modalReducer from './modalReducer';
import ScaffoldsById from './ScaffoldsById';
import scaffoldFeildsReducer from './scaffoldFeildsReducer';
import scaffoldsReducer from './scaffoldsReducer';
import withdrawModalReducer from './withdrawModalReducer';
import globalProperties from "./globalProperties";
import ethAccount from "./ethAccount";
import devShares from './devShares';
import scaffoldTemplates from './scaffoldTemplates';

export default combineReducers({
  form: reduxForm,
  auth: authReducer,
  currencyConversionValue: currencyConversionReducer,
  currentScaffoldInstance: currentScaffoldInstanceReducer,
  modalInfo: modalReducer,
  ScaffoldsById,
  scaffoldTemplates,
  scaffoldFeilds: scaffoldFeildsReducer,
  scaffolds: scaffoldsReducer,
  withdrawModalInfo: withdrawModalReducer,
  ethAccount,
  globalProperties,
  devShares
});
