import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {Field, getFormValues, reduxForm} from 'redux-form';
import {withRouter} from 'react-router-dom';
import {Button, Grid} from 'semantic-ui-react';
import {asyncValidate, validate, validateToken, warn} from '../utils/validation';
import ScaffoldField from '../components-ui/inputs/Field';
import {fetchUserTokens, saveUserToken} from "../actions/user-token";
import {getErc20Token} from "../actions/eth-account";

class UserTokenForm extends Component {
  state = {
    tokenRequest: null,
    address: null
  };
  componentDidMount() {
    this.props.actions.fetchUserTokens();
  }

  onAddressChange = async (e) => {
    console.log(e.target.value);
    this.props.actions.getErc20Token(e.target.value).then(response => {
      this.setState({ tokenRequest: response });
      this.props.initialize({ name: response.name, address: response.address, symbol: response.symbol, decimal: response.decimal });
    });

  }

  handleOnSubmit = async e => {
    const { actions, history, formValues } = this.props;
    e.preventDefault();

    try {
      await actions.saveUserToken(formValues);
      history.push(`/tokens`);
    } catch (e) {
      console.warn('Error occurred during saving application: ', e);
    }

  };

  render() {
    const { invalid } = this.props;
    const disableSubmit = invalid ;
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
                  label="Address*"
                  placeholder="Address"
                  component={ScaffoldField}
                  type="text"
                  name="address"
                  onChange={this.onAddressChange}
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
                  label="Name"
                  placeholder="Name"
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
                  label="Symbol"
                  placeholder="Symbol"
                  component={ScaffoldField}
                  type="text"
                  name="symbol"
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
                  label="Decimal"
                  placeholder="Decimal"
                  component={ScaffoldField}
                  type="number"
                  name="decimal"
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
      </div>
    );
  }
}

const getValues = getFormValues('tokenCreationForm');

const mapStateToProps = state => {
  const formValues = getValues(state) || {};

  return {
    formValues
  };
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(
    {
      getErc20Token,
      saveUserToken,
      fetchUserTokens
    },
    dispatch
  )
});

UserTokenForm = reduxForm({
  validateToken,
  form: 'tokenCreationForm',
  enableReinitialize: true,
})(UserTokenForm);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withRouter(UserTokenForm));
