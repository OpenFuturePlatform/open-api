import React from 'react';
import { withVisible } from '../components-ui/withVisible';
import { Button, Icon, Modal } from 'semantic-ui-react';
import { TransactionError } from '../components-ui/TransactionError';
import { withSaving } from '../components-ui/withSaving';

class EntityRemoveComponent extends React.Component {
  onShowHandle = () => {
    const { onShow, setTransactionError } = this.props;
    setTransactionError('');
    onShow();
  };

  render() {
    const { isVisible, onHide, children, header, isSaving, transactionError, submitWithSaving } = this.props;

    return (
      <span>
        <Icon link name="remove" size="large" onClick={this.onShowHandle} />
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>{header || 'Remove'}</Modal.Header>
          <Modal.Content>
            {children}
            <TransactionError message={transactionError} />
          </Modal.Content>
          <Modal.Actions>
            <Button negative disabled={isSaving} onClick={onHide}>
              Cancel
            </Button>
            <Button
              positive
              loading={isSaving}
              disabled={isSaving}
              icon="checkmark"
              labelPosition="right"
              content="Yes"
              onClick={() => submitWithSaving()}
            />
          </Modal.Actions>
        </Modal>
      </span>
    );
  }
}

export const EntityRemove = withVisible(withSaving(EntityRemoveComponent));
