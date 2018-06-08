import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {Field, FieldArray, getFormValues, reduxForm} from 'redux-form';
import {withRouter} from 'react-router-dom';
import {Button, Grid, Input} from 'semantic-ui-react';
import {DropdownField} from 'react-semantic-redux-form';
import _ from 'lodash';
import {validate, validateScaffoldProperties, warn} from '../../utils/validation';
import ScaffoldActionField from './ScaffoldActionField';
import ScaffoldField from './ScaffoldField';
import ScaffoldPropertyFields from './ScaffoldPropertyFields';
import WrappedInput from './wrappedComponents/WrappedInput';
import {convertCurrencies, deployContract, subscribeEthAccount} from '../../actions';
import {compileContract} from "../../actions/index";

class ScaffoldForm extends Component {

  constructor(props) {
    super(props);
    this.handleOnConvert = this.handleOnConvert.bind(this);
    this.handleOnSubmit = this.handleOnSubmit.bind(this);
  }

  componentDidMount() {
    this.props.actions.subscribeEthAccount();
  }

  componentDidUpdate(prevProps) {
    const prevEthAccount = prevProps.ethAccount;
    const {ethAccount, change} = this.props;

    if (prevEthAccount.account !== ethAccount.account) {
      change('developerAddress', ethAccount.account);
    }
  }

  async handleOnConvert(newCurrency) {
    const {actions, formValues, change} = this.props;
    const conversionAmount = await actions.convertCurrencies({
      fromAmount: formValues.fiatAmount,
      fromCurrency: newCurrency || formValues.currency,
      toCurrency: 'eth'
    });
    change('conversionAmount', conversionAmount);
  }

  async handleOnSubmit(e) {
    const {actions, history, formValues} = this.props;
    e.preventDefault();
    try {
      await actions.deployContract(formValues);
      history.push('/scaffolds')
    } catch (e) {
      console.warn('Deployment Error: ', e);
    }
  }

  render() {
    const {formValues, invalid, scaffoldFieldsErrors, openKeyOptions} = this.props;
    const fieldErrors = _.flatten(scaffoldFieldsErrors).length !== 0 ? true : false;
    const disableSubmit = invalid || fieldErrors;

    return (
      <div>
        <form onSubmit={this.handleOnSubmit}>
          <Grid style={{paddingLeft: '15px'}}>
            <Grid.Row>
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
                       label="Developer Address"
                       placeholder="Developer Address where funds will be sent"
                       component={ScaffoldField}
                       type="text"
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
                       label="Scaffold Title"
                       placeholder="Title shown to customer at checkout"
                       component={ScaffoldField}
                       type="text"
                       name="description"
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
                <Field key={3} action="Converted to Ethereum"
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
                            formValues={formValues.properties || []}/>
              </Grid.Column>
            </Grid.Row>
            <Grid.Row>
              <Grid.Column width={3} floated="right">
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
  const openKey = state.auth ? state.auth.openKeys : undefined;
  const scaffoldFieldsErrors = validateScaffoldProperties(formValues.properties || []);
  const openKeyOptions = state.auth ? state.auth.openKeys
    .filter(it => it.enabled).map(it => ({text: it.value, value: it.value})) : [];
  const ethAccount = state.ethAccount;

  return {
    ethAccount,
    formValues,
    openKey,
    scaffoldFieldsErrors,
    openKeyOptions
  };
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(
    {convertCurrencies, deployContract, subscribeEthAccount, compileContract},
    dispatch,
  ),
});

ScaffoldForm = reduxForm({
  validate,
  warn,
  form: 'scaffoldCreationForm',
  enableReinitialize: true,
  initialValues: {
    currency: 'USD',
    properties: []

    // conversionAmount: 2.0346416588,
    // currency: "USD",
    // description: "hello " + Math.round(Math.random()*1000),
    // developerAddress: "",
    // fiatAmount: "1234",
    // openKey: "op_pk_9d3e3c1e-2770-4eca-8453-0cef89b51591",
    // properties: [{
    //   defaultValue: "1",
    //   name: "prop1",
    //   type: "NUMBER"
    // }],
  }
})(ScaffoldForm);

export default connect(mapStateToProps, mapDispatchToProps)(withRouter(ScaffoldForm));
