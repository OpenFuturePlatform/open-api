import React from 'react';
import {connect} from 'react-redux';
import {Divider, Dropdown} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {setWalletMethod} from '../actions/index';

const WalletSelectComponent = (props) => {
  const {isApiAllowed, byApiMethod, actions} = props;

  if (!isApiAllowed) {
    return null;
  }

  const value = byApiMethod ? 'open' : 'private';
  const onSelect = (e, {value}) => actions.setWalletMethod(value === 'open');

  return (
    <div>
      <Dropdown fluid search selection value={value} onChange={onSelect} options={[
        {key: 'private', text: 'Private Wallet', value: 'private'},
        {key: 'open', text: 'OPEN Platform Wallet', value: 'open'},
      ]}/>
      <Divider/>
    </div>
  )
};

const mapStateToProps = ({auth: {isApiAllowed, byApiMethod}}) => ({isApiAllowed, byApiMethod});

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({setWalletMethod}, dispatch)
});

export const WalletSelect = connect(mapStateToProps, mapDispatchToProps)(WalletSelectComponent);
