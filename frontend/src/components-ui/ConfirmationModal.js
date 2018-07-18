import React from 'react';
import { Modal, Button } from 'semantic-ui-react';
import { TransactionError } from './TransactionError';

export const ConfirmationModal = ({
  isVisible,
  onHide,
  children,
  header,
  isSaving,
  transactionError,
  submitWithSaving,
  submitDisabled
}) => (
  <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
    <Modal.Header>{header || 'Confirmation'}</Modal.Header>
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
        disabled={isSaving || submitDisabled}
        icon="checkmark"
        labelPosition="right"
        content="Yes"
        onClick={() => submitWithSaving()}
      />
    </Modal.Actions>
  </Modal>
);
