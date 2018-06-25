import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {Field, FieldArray, getFormValues, reduxForm} from 'redux-form';
import {withRouter} from 'react-router-dom';
import {Button, Grid, Input} from 'semantic-ui-react';
import {DropdownField} from 'react-semantic-redux-form';
import _ from 'lodash';
import {validate, asyncValidate, validateScaffoldProperties, warn} from '../utils/validation';
import ScaffoldActionField from '../components-ui/inputs/ActionField';
import ScaffoldField from '../components-ui/inputs/Field';
import ScaffoldPropertyFields from '../components-ui/inputs/PropertyFields';
import WrappedInput from '../components-ui/inputs/WrappedInput';
import {convertCurrencies, deployContract, compileContract, deployContractByApi} from '../actions/deploy-contract';
import {subscribeEthAccount, unsubscribeEthAccount} from '../actions/eth-account';
import {MIN_BALANCE} from '../const/index';
import {getMetaMaskError} from '../selectors/getMetaMaskError';
import {fetchTemplates} from '../actions/contract-templates';
import {TemplateSelect} from '../components/TemplateSelect';
import {WalletSelect} from '../components/WalletSelect';

class ScaffoldForm extends Component {

  componentDidMount() {
    if (!this.props.byApiMethod) {
      this.props.actions.subscribeEthAccount();
      this.initDeveloperAddressValidation();
    }
  }

  componentWillUnmount() {
    this.props.actions.unsubscribeEthAccount();
  }

  componentDidUpdate(prevProps) {
    const prevEthAccount = prevProps.ethAccount;
    const {ethAccount, initialValues, byApiMethod, dispatch, blur} = this.props;
    const accountChanged = prevEthAccount.account !== ethAccount.account;
    const networkChanged = prevEthAccount.trueNetwork !== ethAccount.trueNetwork;
    const balanceChanged = prevEthAccount.balance !== ethAccount.balance;
    const initialValuesChanged = prevProps.initialValues !== initialValues;
    const byApiMethodChanged = prevProps.byApiMethod !== byApiMethod;

    if (accountChanged || networkChanged || balanceChanged || initialValuesChanged) {
      this.initDeveloperAddressValidation();
      this.handleOnConvert(initialValues.currency, initialValues.fiatAmount || 0);
    }

    if (byApiMethodChanged && byApiMethod) {
      dispatch(blur('developerAddress', ''));
    }

    if (byApiMethodChanged && !byApiMethod) {
      this.initDeveloperAddressValidation();
    }
  }

  initDeveloperAddressValidation = () => {
    const {ethAccount, blur, dispatch} = this.props;
    dispatch(blur('developerAddress', ethAccount.account));
  };

  validateBalance = () => {
    const {ethAccount, metaMaskError} = this.props;

    if (metaMaskError) {
      return metaMaskError;
    }

    return ethAccount.ethBalance < MIN_BALANCE ?
      'Minimum balance: 0,0087 Eth. Change MetaMask account or top up the balance.'
      : null;
  };

  handleOnConvert = async (newCurrency, fiatAmount) => {
    const {actions, formValues, change} = this.props;
    const conversionAmount = await actions.convertCurrencies({
      fromAmount: (fiatAmount || fiatAmount === 0) ? fiatAmount : formValues.fiatAmount,
      fromCurrency: newCurrency || formValues.currency,
      toCurrency: 'eth'
    });
    change('conversionAmount', conversionAmount);
  };

  handleOnSubmit = async (e) => {
    const {actions, history, formValues, byApiMethod} = this.props;
    e.preventDefault();
    try {
      let contractAddress;

      if (byApiMethod) {
        contractAddress = await actions.deployContractByApi(formValues);
      } else {
        contractAddress = await actions.deployContract(formValues);
      }
      history.push(`/scaffolds/${contractAddress}`);
    } catch (e) {
      console.warn('Deployment Error: ', e);
    }
  };

  render() {
    const {formValues, invalid, scaffoldFieldsErrors, openKeyOptions, byApiMethod} = this.props;
    const fieldErrors = _.flatten(scaffoldFieldsErrors).length !== 0 ? true : false;
    const disableSubmit = invalid || fieldErrors;
    const developerAddressValidations = !byApiMethod ? [this.validateBalance] : [];

    return (
      <div>
        <WalletSelect/>
        <form onSubmit={this.handleOnSubmit}>
          <Grid style={{paddingLeft: '15px'}}>
            <Grid.Row>
              <Grid.Column width={16} style={{paddingTop: '10px'}}>
                <TemplateSelect/>
              </Grid.Column>
              <Grid.Column width={16} style={{paddingTop: '10px'}}>
                <Field key={1}
                       className="ui selection fluid dropdown"
                       placeholder="Choose Developer API Key"
                       component={DropdownField}
                       options={openKeyOptions}
                       type="text"
                       name="openKey"/>
              </Grid.Column>
              <Grid.Column width={16}>
                <Field key={2}
                       labelStyle={{
                         width: '187px',
                         maxHeight: '38px',
                         marginTop: '10px',
                         marginBottom: '5px',
                       }}
                       inputStyle={{
                         marginTop: '10px',
                         marginBottom: '5px',
                       }}
                       label="Developer Address*"
                       placeholder="Developer Address where funds will be sent"
                       component={ScaffoldField}
                       type="text"
                       validate={developerAddressValidations}
                       name="developerAddress"/>
              </Grid.Column>
              <Grid.Column width={16}>
                <Field key={3}
                       labelStyle={{
                         width: '187px',
                         maxHeight: '38px',
                         marginTop: '10px',
                         marginBottom: '5px',
                       }}
                       inputStyle={{
                         marginTop: '10px',
                         marginBottom: '5px',
                       }}
                       label="Scaffold Title*"
                       placeholder="Title shown to customer at checkout"
                       component={ScaffoldField}
                       type="text"
                       name="description"
                />
              </Grid.Column>
              <Grid.Column width={16}>
                <Field key={3}
                       labelStyle={{
                         width: '187px',
                         maxHeight: '38px',
                         marginTop: '10px',
                         marginBottom: '5px',
                       }}
                       inputStyle={{
                         marginTop: '10px',
                         marginBottom: '5px',
                       }}
                       label="Webhook"
                       placeholder="Webhook"
                       component={ScaffoldField}
                       type="text"
                       name="webHook"
                />
              </Grid.Column>
            </Grid.Row>
            <Grid.Row>
              <Grid.Column width={8}>
                <Input className="ui fluid action input">
                  <Field key={4}
                         name="fiatAmount"
                         placeholder="Fiat Amount of Scaffold"
                         component={WrappedInput}
                         style={{width: '272px'}}
                         callback={this.handleOnConvert}/>
                  <Field key={5}
                         className="ui compact selection dropdown"
                         name="currency"
                         options={[
                           {key: 'dollars', text: '$', value: 'USD'},
                           {key: 'pounds', text: '£', value: 'GBP'},
                           {key: 'eruo', text: '€', value: 'EUR'},
                           {key: 'reminibi', text: '¥', value: 'CNY'},
                           {key: 'ether', text: '♦', value: 'ETH'},
                         ]}
                         component={DropdownField}
                         onChange={(e, value) => this.handleOnConvert(value)}/>
                </Input>
              </Grid.Column>
              <Grid.Column width={8}>
                <Field key={3} action="Converted to Ethereum*"
                       placeholder="Fiat in Ether"
                       component={ScaffoldActionField}
                       disableInput={true}
                       type="text"
                       name="conversionAmount"/>
              </Grid.Column>
            </Grid.Row>
            <Grid.Row>
              <Grid.Column width={16}>
                <FieldArray name="properties"
                            component={ScaffoldPropertyFields}
                            scaffoldFieldsErrors={scaffoldFieldsErrors}
                            scaffoldProperties={formValues.properties || []}/>
              </Grid.Column>
            </Grid.Row>
            <Grid.Row>
              <Grid.Column width={2} floated="right">
                <Button type="submit" primary disabled={disableSubmit}
                        style={{
                          marginBottom: '10px',
                          marginRight: '15px',
                          backgroundColor: '#3193F5',
                        }}>
                  Submit
                </Button>
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </form>
      </div>
    );
  }
}

const getValues = getFormValues('scaffoldCreationForm');

const mapStateToProps = (state) => {
  const formValues = getValues(state) || {};
  const initialValues = state.scaffoldFeilds;
  const openKey = state.auth ? state.auth.openKeys : undefined;
  const scaffoldFieldsErrors = validateScaffoldProperties(formValues.properties || []);
  const openKeyOptions = state.auth ? state.auth.openKeys
    .filter(it => it.enabled).map(it => ({text: it.value, value: it.value})) : [];
  const ethAccount = state.ethAccount;
  const byApiMethod = state.auth.byApiMethod;
  const metaMaskError = getMetaMaskError(state);

  return {
    initialValues,
    metaMaskError,
    byApiMethod,
    ethAccount,
    formValues,
    openKey,
    scaffoldFieldsErrors,
    openKeyOptions
  };
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(
    {
      convertCurrencies,
      deployContract,
      subscribeEthAccount,
      compileContract,
      unsubscribeEthAccount,
      deployContractByApi,
      fetchTemplates
    },
    dispatch,
  ),
});

ScaffoldForm = reduxForm({
  validate,
  warn,
  form: 'scaffoldCreationForm',
  enableReinitialize: true,
  asyncValidate,
  asyncBlurFields: ['webHook']
})(ScaffoldForm);

export default connect(mapStateToProps, mapDispatchToProps)(withRouter(ScaffoldForm));
