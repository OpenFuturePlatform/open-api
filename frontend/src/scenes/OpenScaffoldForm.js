import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {Field, FieldArray, getFormValues, reduxForm} from 'redux-form';
import {withRouter} from 'react-router-dom';
import {Button, Grid} from 'semantic-ui-react';
import {DropdownField} from 'react-semantic-redux-form';
import {asyncValidate, validate, validateScaffoldProperties, warn} from '../utils/validation';
import ScaffoldField from '../components-ui/inputs/Field';
import {saveOpenScaffold} from '../actions/deploy-contract';
import {fetchKeys} from '../actions/keys';
import {TemplateSelect} from "../components/TemplateSelect";
import ScaffoldPropertyFields from "../components-ui/inputs/PropertyFields";

class OpenScaffoldForm extends Component {

  componentDidMount() {
    this.props.actions.fetchKeys();
  }

  handleOnSubmit = async e => {
    const {actions, history, formValues} = this.props;
    e.preventDefault();

    try {
      await actions.saveOpenScaffold(formValues);
      history.push(`/scaffolds/open`);
    } catch (e) {
      console.warn('Error occurred during saving scaffold: ', e);
    }
  };

  render() {
    const { invalid: disableSubmit,formValues, scaffoldFieldsErrors, openKeyOptions } = this.props;

    return (
      <form onSubmit={this.handleOnSubmit}>
        <Grid style={{paddingLeft: '15px'}}>
          <Grid.Row>
            <Grid.Column width={16} style={{ paddingTop: '10px' }}>
              <TemplateSelect />
            </Grid.Column>
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
                key={2}
                labelStyle={{
                  width: '187px',
                  maxHeight: '38px',
                  marginTop: '10px',
                  marginBottom: '5px'
                }}
                inputStyle={{
                  marginTop: '10px',
                  marginBottom: '5px'
                }}
                label="Developer Address*"
                placeholder="Developer Address where funds will be sent"
                component={ScaffoldField}
                type="text"
                name="developerAddress"
              />
            </Grid.Column>
            <Grid.Column width={16}>
              <Field
                key={3}
                labelStyle={{
                  width: '187px',
                  maxHeight: '38px',
                  marginTop: '10px',
                  marginBottom: '5px'
                }}
                inputStyle={{
                  marginTop: '10px',
                  marginBottom: '5px'
                }}
                label="Scaffold Title*"
                placeholder="Title shown to customer at checkout"
                component={ScaffoldField}
                type="text"
                name="title"
              />
            </Grid.Column>
            <Grid.Column width={16}>
              <Field
                key={3}
                labelStyle={{
                  width: '187px',
                  maxHeight: '38px',
                  marginTop: '10px',
                  marginBottom: '5px'
                }}
                inputStyle={{
                  marginTop: '10px',
                  marginBottom: '5px'
                }}
                label="Webhook"
                placeholder="Webhook"
                component={ScaffoldField}
                type="text"
                name="webHook"
              />
            </Grid.Column>
            <Grid.Column width={16}>
              <Field
                labelStyle={{
                  width: '187px',
                  maxHeight: '38px',
                  marginTop: '10px',
                  marginBottom: '5px'
                }}
                inputStyle={{
                  marginTop: '10px',
                  marginBottom: '5px'
                }}
                key={5}
                label="Openc*"
                placeholder="Fiat Amount of Scaffold"
                component={ScaffoldField}
                name="fiatAmount"
              />
            </Grid.Column>
          </Grid.Row>
          <Grid.Row>
            <Grid.Column width={16}>
              <FieldArray
                name="properties"
                component={ScaffoldPropertyFields}
                scaffoldFieldsErrors={scaffoldFieldsErrors}
                scaffoldProperties={formValues.properties || []}
              />
            </Grid.Column>
          </Grid.Row>
          <Grid.Row>
            <Grid.Column width={2} floated="right">
              <Button
                type="submit"
                primary
                disabled={disableSubmit}
                style={{
                  marginBottom: '10px',
                  marginRight: '15px',
                  backgroundColor: '#3193F5'
                }}
              >
                Submit
              </Button>
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </form>
    );
  }
}

const getValues = getFormValues('openScaffoldCreationForm');

const mapStateToProps = state => {
  const formValues = getValues(state) || {};
  const initialValues = state.scaffoldFeilds;
  const openKey = state.auth ? state.auth.openKeys : undefined;
  const scaffoldFieldsErrors = validateScaffoldProperties(formValues.properties || []);
  const openKeyOptions = state.keys.filter(it => it.enabled).map(it => ({text: it.value, value: it.value}));

  return {
    formValues,
    initialValues,
    openKey,
    scaffoldFieldsErrors,
    openKeyOptions
  };
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(
    {
      saveOpenScaffold: saveOpenScaffold,
      fetchKeys
    },
    dispatch
  )
});

OpenScaffoldForm = reduxForm({
  validate,
  warn,
  form: 'openScaffoldCreationForm',
  enableReinitialize: true,
  asyncValidate,
  asyncBlurFields: ['webHook']
})(OpenScaffoldForm);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withRouter(OpenScaffoldForm));
