import React from 'react';
import { withVisible } from '../components-ui/withVisible';
import { Button, Divider, Input, Modal } from 'semantic-ui-react';
import { validateAddress } from '../utils/validation';
import { TransactionError } from '../components-ui/TransactionError';
import { withSaving } from '../components-ui/withSaving';
import { ShowAddEditButton } from '../components-ui/ShowAddEditButton';
import { ErrorMessage } from '../components-ui/ErrorMessage';

class ShareHolderSaveComponent extends React.Component {
  constructor(props) {
    super(props);

    this.state = this.getDefaultState();
  }

  getDefaultState = () => {
    const address = this.props.editType ? this.props.shareHolder.address : '';
    const share = this.props.editType ? this.props.shareHolder.share : '';

    return {
      address,
      share,
      isAddressErrorVisible: false
    };
  };

  getAvailableShare = () => {
    const { allHolders = [], shareHolder } = this.props;
    const shareOfSelected = shareHolder ? Number(shareHolder.share) : 0;
    const sumShares = allHolders.reduce((sum, it) => sum + Number(it.share), 0);
    return 100 - sumShares + shareOfSelected;
  };

  validateShare = () => {
    const availableShare = this.getAvailableShare();
    const { share } = this.state;
    if (share > availableShare) {
      return [`Available share: ${availableShare}%`];
    }
    return [];
  };

  onAddressChange = e => this.setState({ address: e.target.value });
  onShareChange = e => this.setState({ share: Math.abs(e.target.value) || '' });
  onAddressBlur = () => this.setState({ isAddressErrorVisible: true });

  onShowHandler = () => {
    const { onShow, setTransactionError } = this.props;
    this.setState(this.getDefaultState());
    setTransactionError('');
    onShow();
  };

  onSubmit = async () => {
    const { submitWithSaving } = this.props;
    const { address, share } = this.state;
    this.setState({ isSaving: true });
    submitWithSaving({ address, share });
  };

  render() {
    const { isVisible, onHide, editType, transactionError, isSaving } = this.props;
    const { share, address, isAddressErrorVisible } = this.state;
    const title = editType ? 'Edit Share Holder' : 'New Share Holder';
    const addressErrorList = validateAddress(address);
    const shareErrorList = this.validateShare();
    const submitDisabled = isSaving || !address || !share || !!shareErrorList.length || !!addressErrorList.length;

    return (
      <span>
        <ShowAddEditButton editType={editType} onShow={this.onShowHandler} />
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>{title}</Modal.Header>
          <Modal.Content>
            <div>Address:</div>
            <Input
              fluid
              value={address}
              disabled={isSaving || editType}
              onChange={this.onAddressChange}
              onBlur={this.onAddressBlur}
            />
            <ErrorMessage errorList={addressErrorList} isVisible={isAddressErrorVisible} />
            <div>Share:</div>
            <Input
              type="number"
              min={0}
              step={1}
              fluid
              value={share}
              disabled={isSaving}
              onChange={this.onShareChange}
            />
            <ErrorMessage errorList={shareErrorList} isVisible={shareErrorList.length} />
            <Divider />
            <span>PS: Please be patient this may take a while...</span>
            <TransactionError message={transactionError} />
          </Modal.Content>
          <Modal.Actions>
            <Button negative disabled={isSaving} onClick={onHide}>
              Cancel
            </Button>
            <Button
              positive
              loading={isSaving}
              disabled={submitDisabled}
              icon="checkmark"
              labelPosition="right"
              content="Save"
              onClick={this.onSubmit}
            />
          </Modal.Actions>
        </Modal>
      </span>
    );
  }
}

export const ShareHolderSave = withVisible(withSaving(ShareHolderSaveComponent));
