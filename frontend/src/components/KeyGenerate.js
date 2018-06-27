import React from 'react';
import { Button, Modal } from 'semantic-ui-react';
import { Datepicker } from '../components-ui/Datepicker';
import styled from 'styled-components';
import { withVisible } from '../components-ui/withVisible';
import { withSaving } from '../components-ui/withSaving';

const KeyGenerateContainer = styled.div`
  overflow: hidden;
  padding-bottom: 15px;
`;

const KeyGenerateComponent = props => {
  const { isVisible, onHide, onShow, isSaving, submitWithSaving } = props;

  return (
    <KeyGenerateContainer>
      <Button primary type="button" floated="right" onClick={onShow}>
        Generate New Key
      </Button>
      <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
        <Modal.Header>Generate Key</Modal.Header>
        <Datepicker />
        <Modal.Content>{/* {this.renderTransactionError()} */}</Modal.Content>
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
            onClick={submitWithSaving}
          />
        </Modal.Actions>
      </Modal>
    </KeyGenerateContainer>
  );
};

export const KeyGenerate = withVisible(withSaving(KeyGenerateComponent));
