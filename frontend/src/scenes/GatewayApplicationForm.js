import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {Field, getFormValues, reduxForm} from 'redux-form';
import {withRouter} from 'react-router-dom';
import {Button, Grid} from 'semantic-ui-react';
import {DropdownField} from 'react-semantic-redux-form';
import {asyncValidate, validate, warn} from '../utils/validation';
import ScaffoldActionField from '../components-ui/inputs/ActionField';
import ScaffoldField from '../components-ui/inputs/Field';
import {fetchGatewayApplications, saveGatewayApplication} from "../actions/gateways";

class GatewayApplicationForm extends Component {
    componentDidMount() {
        this.props.actions.fetchGatewayApplications();
    }

    handleOnSubmit = async e => {
        const { actions, history, formValues } = this.props;
        e.preventDefault();

        try {
            await actions.saveGateway(formValues);
            history.push(`/applications`);
        } catch (e) {
            console.warn('Error occurred during saving application: ', e);
        }

    };

    render() {
        const { invalid } = this.props;
        return (
            <div>
                <form onSubmit={this.handleOnSubmit}>
                    <Grid style={{ paddingLeft: '15px' }}>
                        <Grid.Row>

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
                                    label="Gateway Title*"
                                    placeholder="Title shown to customer at checkout"
                                    component={ScaffoldField}
                                    type="text"
                                    name="name"
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
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column width={2} floated="right">
                                <Button
                                    type="submit"
                                    primary
                                    disabled={invalid}
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
            </div>
        );
    }
}

const getValues = getFormValues('gatewayCreationForm');

const mapStateToProps = state => {
    const formValues = getValues(state) || {};
    const initialValues = state.gateways;

    return {
        initialValues,
        formValues
    };
};

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(
        {
            saveGateway: saveGatewayApplication,
            fetchGatewayApplications
        },
        dispatch
    )
});

GatewayApplicationForm = reduxForm({
    validate,
    warn,
    form: 'gatewayCreationForm',
    enableReinitialize: true,
    asyncValidate,
    asyncBlurFields: ['webHook']
})(GatewayApplicationForm);

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(withRouter(GatewayApplicationForm));
