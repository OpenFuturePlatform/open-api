import React, { Component } from 'react';
import { Icon, Modal, Button, Input } from 'semantic-ui-react';
import { withVisible } from '../components-ui/withVisible';
import { withSaving } from '../components-ui/withSaving';
import { TransactionError } from '../components-ui/TransactionError';

class ScaffoldEditComponent extends Component {
  state = {
    description: ''
  };

  onShow = () => {
    const { onShow, description } = this.props;
    this.setState({ description });
    onShow();
  };

  isSubmitEnabled = () => {
    const { isSaving } = this.props;
    const { description } = this.state;
    return isSaving || !description || description === this.props.description;
  };

  onDescriptionChange = e => this.setState({ description: e.target.value });

  onSubmit = () => this.props.submitWithSaving({ description: this.state.description });

  render() {
    const { isVisible, isSaving, onHide, transactionError } = this.props;
    const { description } = this.state;

    return (
      <span>
        <Icon link name="edit" onClick={this.onShow} />
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>Edit Scaffold</Modal.Header>
          <Modal.Content>
            <div>Address:</div>
            <Input fluid value={description} disabled={isSaving} onChange={this.onDescriptionChange} />
            <TransactionError message={transactionError} />
          </Modal.Content>
          <Modal.Actions>
            <Button negative disabled={isSaving} onClick={onHide}>
              Cancel
            </Button>
            <Button
              positive
              loading={isSaving}
              disabled={this.isSubmitEnabled()}
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
