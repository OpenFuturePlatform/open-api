// ScaffoldForm shows a form for a user to add custom scaffold properties
import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {Field, FieldArray, formValueSelector, getFormMeta, reduxForm} from 'redux-form';
import {withRouter} from 'react-router-dom';
import {Button, Grid, Input} from 'semantic-ui-react';
import {DropdownField} from 'react-semantic-redux-form';
import _ from 'lodash';
import {validate, validateScaffoldProperties, warn} from '../../utils/validation';
// Components
import ScaffoldActionField from './ScaffoldActionField';
import ScaffoldField from './ScaffoldField';
import ScaffoldPropertyFields from './ScaffoldPropertyFields';
import WrappedInput from './wrappedComponents/WrappedInput';
// Actions
import {convertCurrencies, deployContract, generatePublicKey,} from '../../actions';

class ScaffoldForm extends Component {
  render() {
    const {
      formValues,
      actions,
      history,
      invalid,
      scaffoldFieldsErrors,
      formMetaData,
      openKeyOptions
    } = this.props;
    const fieldErrors = _.flatten(scaffoldFieldsErrors).length !== 0 ? true : false;
    const disableSubmit = invalid || fieldErrors;

    return (
      <div>
        <form>
          <Grid style={{paddingLeft: '15px'}}>
            <Grid.Row>
              <Grid.Column width={16} style={{paddingTop: '10px'}}>
                <Field
                  key={1}
                  className="ui selection fluid dropdown"
                  placeholder="Choose Developer API Key"
                  component={DropdownField}
                  options={openKeyOptions}
                  type="text"
                  name="openKey"
                />
              </Grid.Column>
              <Grid.Column width={16}>
                <Field
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
                  key={2}
                  label="Developer Address"
                  placeholder="Developer Address where funds will be sent"
                  component={ScaffoldField}
                  type="text"
                  name="developerAddress"
                />
              </Grid.Column>
              <Grid.Column width={16}>
                <Field
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
                  key={3}
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
                  <Field
                    key={4}
                    name="fiatAmount"
                    placeholder="Fiat Amount of Scaffold"
                    component={WrappedInput}
                    style={{width: '272px'}}
                    callback={actions.convertCurrencies}
                    formValues={{
                      fromAmount: formValues.fiatAmount,
                      fromCurrency: formValues.currency,
                      toCurrency: 'eth'
                    }}
                  />
                  <Field
                    key={5}
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
                    onChange={(e, value) => actions.convertCurrencies({
                        fromAmount: formValues.fiatAmount,
                        fromCurrency: value,
                        toCurrency: 'eth'
                      })}
                  />
                </Input>
              </Grid.Column>
              <Grid.Column width={8}>
                <Field
                  key={3}
                  action="Converted to Ethereum"
                  placeholder="Fiat in Ether"
                  component={ScaffoldActionField}
                  disableInput={true}
                  type="text"
                  name="conversionAmount"
                />
              </Grid.Column>
            </Grid.Row>
            <Grid.Row>
              <Grid.Column width={16}>
                <FieldArray
                  name="properties"
                  component={ScaffoldPropertyFields}
                  datatypeOptions={[
                    {key: 'string', text: 'string', value: 'STRING'},
                    {key: 'number', text: 'number', value: 'NUMBER'},
                    {key: 'boolean', text: 'boolean', value: 'BOOLEAN'},
                  ]}
                  scaffoldFieldsErrors={scaffoldFieldsErrors}
                  formMetaData={formMetaData}
                  formValues={formValues}
                />
              </Grid.Column>
            </Grid.Row>
            <Grid.Row>
              <Grid.Column width={3} floated="right">
                <Button
                  primary
                  disabled={disableSubmit}
                  style={{
                    marginBottom: '10px',
                    marginRight: '15px',
                    backgroundColor: '#3193F5',
                  }}
                  type="button"
                  onClick={() =>
                    actions.deployContract(
                      formValues,
                      history,
                    )
                  }
                >
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

ScaffoldForm = reduxForm({
  validate,
  warn,
  form: 'scaffoldCreationForm',
  enableReinitialize: true
})(ScaffoldForm);

const selector = formValueSelector('scaffoldCreationForm');
const formMeta = getFormMeta('scaffoldCreationForm');

const mapStateToProps = (state) => {
  let formValues = {};
  const datatypeOptions = state.datatypeOptions;
  const openKey = state.auth ? state.auth.openKeys : undefined;
  const formMetaData = formMeta(state);

  formValues.conversionAmount = selector(
    state,
    'conversionAmount',
  );
  formValues.properties = selector(state, 'properties') || [];
  formValues.fiatAmount = selector(state, 'fiatAmount');
  formValues.currency = selector(state, 'currency');
  formValues.openKey = selector(state, 'openKey');
  formValues.developerAddress = selector(state, 'developerAddress');
  formValues.description = selector(state, 'description');

  const scaffoldFieldsErrors = validateScaffoldProperties(formValues.properties);

  const initialValues = {
    openKey: formValues.openKey,
    developerAddress: formValues.developerAddress,
    description: formValues.description,
    conversionAmount: state.currencyConversionValue,
    fiatAmount: formValues.fiatAmount,
    properties: formValues.properties,
    datatypeOptions: state.datatypeOptions,
    currency: formValues.currency || 'USD',
  };

  const openKeyOptions = state.auth ? state.auth.openKeys
    .filter(it => it.enabled).map(it => ({text: it.value, value: it.value})) : [];

  return {
    initialValues,
    formValues,
    datatypeOptions,
    openKey,
    scaffoldFieldsErrors,
    formMetaData,
    openKeyOptions
  };
};


const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(
    {generatePublicKey, convertCurrencies, deployContract},
    dispatch,
  ),
});

ScaffoldForm = connect(mapStateToProps, mapDispatchToProps)(
  withRouter(ScaffoldForm),
);

export default ScaffoldForm;
