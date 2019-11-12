import React, {Component} from 'react';
import {Button, Icon, Input, Modal} from 'semantic-ui-react';
import {withVisible} from '../components-ui/withVisible';
import {withSaving} from '../components-ui/withSaving';
import {TransactionError} from '../components-ui/TransactionError';
import {ErrorMessage} from '../components-ui/ErrorMessage';

class EthereumScaffoldEditComponent extends Component {
  state = {
    title: ''
  };

  onShow = () => {
    const { onShow, scaffold } = this.props;
    const { title } = scaffold;
    this.setState({ title });
    onShow();
  };

  isSubmitDisabled = () => {
    const { isSaving } = this.props;
    const { scaffold } = this.props;
    const { title } = scaffold;
    return isSaving || !this.state.title || title === this.state.title;
  };

  onTitleChange = e => this.setState({ title: e.target.value });

  onSubmit = () => this.props.submitWithSaving({ title: this.state.title });

  render() {
    const { isVisible, isSaving, onHide, transactionError, fieldErrors } = this.props;
    const titleErrorList = fieldErrors.title || [];
    const { title } = this.state;

    return (
      <span>
        <Icon link name="edit" onClick={this.onShow} />
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>Edit Scaffold</Modal.Header>
          <Modal.Content>
            <div>Title:</div>
            <Input fluid value={title} disabled={isSaving} onChange={this.onTitleChange} />
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

export const EthereumScaffoldEdit = withVisible(withSaving(EthereumScaffoldEditComponent));
