import React, { Component } from 'react';
import { Button, Modal } from 'semantic-ui-react';
import { Datepicker } from '../components-ui/Datepicker';
import styled from 'styled-components';
import { withVisible } from '../components-ui/withVisible';
import { withSaving } from '../components-ui/withSaving';
import { TransactionError } from '../components-ui/TransactionError';

const KeyGenerateContainer = styled.div`
  overflow: hidden;
  padding-bottom: 15px;
`;

class KeyGenerateComponent extends Component {
  state = {
    expiredDate: null,
    transactionError: ''
  };

  onShow = () => {
    const { onShow, setTransactionError } = this.props;
    this.setState({ expiredDate: null });
    setTransactionError('');
    onShow();
  };

  onBackgroundClick = e => {
    const { onHide, isSaving } = this.props;
    const target = e.target;
    if (isSaving || !target.classList.contains('modals')) {
      return;
    }
    onHide();
  };

  render() {
    const { isVisible, onHide, isSaving, submitWithSaving, transactionError } = this.props;
    const { expiredDate } = this.state;

    return (
      <KeyGenerateContainer>
        <Button primary type="button" floated="right" onClick={this.onShow}>
          Generate New Key
        </Button>
        <Modal size="tiny" open={isVisible} onClose={this.onBackgroundClick}>
          <Modal.Header>Generate Key</Modal.Header>
          <Modal.Content>
            <Datepicker date={this.state.expiredDate} onChange={expiredDate => this.setState({ expiredDate })} />
            <TransactionError message={transactionError} />
          </Modal.Content>
          <Modal.Actions>
            <Button negative disabled={isSaving} onClick={onHide}>
              Cancel
            </Button>
            <Button
              positive
              loading={isSaving}
              icon="checkmark"
              labelPosition="right"
              content="Save"
              onClick={() => submitWithSaving({ expiredDate })}
            />
          </Modal.Actions>
        </Modal>
      </KeyGenerateContainer>
    );
  }
}

export const KeyGenerate = withVisible(withSaving(KeyGenerateComponent));
