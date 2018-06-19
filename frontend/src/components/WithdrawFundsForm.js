import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {Field, formValueSelector, reduxForm} from 'redux-form';
import {Button, Form, Input, Message} from 'semantic-ui-react';
import {DropdownField} from 'react-semantic-redux-form';
import {withRouter} from 'react-router-dom';
import {withdrawFunds} from '../actions';
import ScaffoldPropertyField from "../components-ui/inputs/PropertyField";

class WithdrawFundsForm extends Component {
  render() {
    const {actions, formValues, currentScaffoldInstance, history} = this.props;
    return (
      <Form>
        <Input className="ui fluid action input">
          <Field
            key={4}
            name="withdrawalAmount"
            placeholder="Amount in"
            component={ScaffoldPropertyField}
            style={{maxHeight: '38px'}}
          />
          <Field
            key={5}
            role="listbox"
            className="ui compact selection dropdown"
            name="withdrawalCurrency"
            options={[
              {key: 'dollars', text: '$', value: 'USD'},
              {key: 'pounds', text: '£', value: 'GBP'},
              {key: 'eruo', text: '€', value: 'EUR'},
              {key: 'reminibi', text: '¥', value: 'CNY'},
              {key: 'ether', text: '♦', value: 'ETH'},
            ]}
            component={DropdownField}
            style={{maxHeight: '38px'}}
          />
          <Button
            onClick={() => {
              actions.withdrawFunds(formValues, currentScaffoldInstance, history)
            }}
            style={{maxHeight: '38px'}} type="button" className="ui button">Withdraw</Button>
        </Input>
        <Message
          error
          header="There was some errors with your submission"
        />
      </Form>
    );
  }
}

WithdrawFundsForm = reduxForm({
  form: 'WithdrawFundsForm',
})(WithdrawFundsForm);

const selector = formValueSelector('WithdrawFundsForm');

const mapStateToProps = state => {
  let formValues = {};
  formValues.withdrawalAmount = selector(state, 'withdrawalAmount');
  formValues.withdrawalCurrency = selector(state, 'withdrawalCurrency');

  const {currentScaffoldInstance} = state;

  return {formValues, currentScaffoldInstance};
};

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({withdrawFunds}, dispatch),
});

WithdrawFundsForm = connect(mapStateToProps, mapDispatchToProps)(
  withRouter(WithdrawFundsForm),
);

export default WithdrawFundsForm;
