import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {Field, FieldArray, getFormValues, reduxForm} from 'redux-form';
import {withRouter} from 'react-router-dom';
import {Button, Grid} from 'semantic-ui-react';
import {asyncValidate, validate, validateScaffoldProperties, warn} from '../utils/validation';
import ScaffoldField from '../components-ui/inputs/Field';
import {saveOpenScaffold} from '../actions/deploy-contract';
import {TemplateSelect} from "../components/TemplateSelect";
import ScaffoldPropertyFields from "../components-ui/inputs/PropertyFields";

class OpenScaffoldForm extends Component {

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
    const { invalid: disableSubmit,formValues, scaffoldFieldsErrors } = this.props;

    return (
      <form onSubmit={this.handleOnSubmit}>
        <Grid style={{paddingLeft: '15px'}}>
          <Grid.Row>
            <Grid.Column width={16} style={{ paddingTop: '10px' }}>
              <TemplateSelect />
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
  const scaffoldFieldsErrors = validateScaffoldProperties(formValues.properties || []);

  return {
    formValues,
    initialValues,
    scaffoldFieldsErrors,
  };
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(
    {
      saveOpenScaffold: saveOpenScaffold
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
