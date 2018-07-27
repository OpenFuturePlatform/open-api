import React from 'react';
import { withVisible } from '../components-ui/withVisible';
import { Button, Divider } from 'semantic-ui-react';
import { withSaving } from '../components-ui/withSaving';
import { ConfirmationModal } from '../components-ui/ConfirmationModal';
import { TransactionError } from '../components-ui/TransactionError';
import { MIN_CONTRACT_DEPOSIT } from '../const';
import { validateMMTokenBalance } from '../selectors/getMetaMaskError';
import { topUpTokenBalance } from '../actions/scaffold-activation';
import { connect } from 'react-redux';
import styled from 'styled-components';
import { t } from '../utils/messageTexts';

const ErrorMessage = styled.div`
  display: inline-block;
  padding-left: 30px;
  color: red;
  vertical-align: top;
`;

const ScaffoldTopUpTokensComponent = ({ children, topUpError, ...props }) => (
  <div>
    <span>
      <Button primary onClick={props.onShow}>
        Top up token balance
      </Button>
      <ConfirmationModal {...props} submitDisabled={!!topUpError}>
        <div>
          {t('sure to top up balance')}
          <Divider />
          <span>{t('it may take a while')}</span>
          <TransactionError>{topUpError}</TransactionError>
        </div>
      </ConfirmationModal>
    </span>
    <ErrorMessage>
      {t('scaffold not activated')}
      <br />
      {t('to activate you need tokens', MIN_CONTRACT_DEPOSIT)}
    </ErrorMessage>
  </div>
);

const mapStateToProps = state => ({ topUpError: validateMMTokenBalance(state) });

const mapDispatchToProps = (dispatch, { scaffold }) => ({
  onSubmit: () => dispatch(topUpTokenBalance(scaffold))
});

export const ScaffoldTopUpTokens = connect(
  mapStateToProps,
  mapDispatchToProps
)(withVisible(withSaving(ScaffoldTopUpTokensComponent)));
