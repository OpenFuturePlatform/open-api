import React, { Component } from 'react';
import { Icon, Modal, Button, Input } from 'semantic-ui-react';
import { withVisible } from '../components-ui/withVisible';
import { withSaving } from '../components-ui/withSaving';
import { TransactionError } from '../components-ui/TransactionError';
import { ErrorMessage } from '../components-ui/ErrorMessage';

class ScaffoldEditComponent extends Component {
  state = {
    description: ''
  };

  onShow = () => {
    const { onShow, scaffold } = this.props;
    const { description } = scaffold;
    this.setState({ description });
    onShow();
  };

  isSubmitDisabled = () => {
    const { isSaving } = this.props;
    const { scaffold } = this.props;
    const { description } = scaffold;
    return isSaving || !this.state.description || description === this.state.description;
  };

  onDescriptionChange = e => this.setState({ description: e.target.value });

  onSubmit = () => this.props.submitWithSaving({ description: this.state.description });

  render() {
    const { isVisible, isSaving, onHide, transactionError, fieldErrors } = this.props;
    const titleErrorList = fieldErrors.description || [];
    const { description } = this.state;

    return (
      <span>
        <Icon link name="edit" onClick={this.onShow} />
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>Edit Scaffold</Modal.Header>
          <Modal.Content>
            <div>Title:</div>
            <Input fluid value={description} disabled={isSaving} onChange={this.onDescriptionChange} />
            <ErrorMessage errorList={titleErrorList} isVisible={titleErrorList.length} />
            <TransactionError message={transactionError} />
          </Modal.Content>
          <Modal.Actions>
            <Button negative disabled={isSaving} onClick={onHide}>
              Cancel
            </Button>
            <Button
              positive
              loading={isSaving}
              disabled={this.isSubmitDisabled()}
              icon="checkmark"
              labelPosition="right"
              content="Yes"
              onClick={this.onSubmit}
            />
          </Modal.Actions>
        </Modal>
      </span>
    );
  }
}

export const ScaffoldEdit = withVisible(withSaving(ScaffoldEditComponent));
