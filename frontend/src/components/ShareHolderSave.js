import React from 'react';
import {withVisible} from '../components-ui/withVisible';
import {Button, Divider, Icon, Input, Message, Modal} from 'semantic-ui-react';
import {validateAddress} from '../utils/validation';

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
      isAddressErrorVisible: false,
      isSaving: false,
      transactionError: ''
    }
  };

  onAddressChange = e => this.setState({address: e.target.value});
  onShareChange = e => this.setState({share: Math.abs(e.target.value)});
  onAddressBlur = e => this.setState({isAddressErrorVisible: true});

  onShowHandle = () => {
    const {onShow} = this.props;
    this.setState(this.getDefaultState());
    onShow();
  };

  onSubmit = async e => {
    const {onSubmit} = this.props;
    const {address, share} = this.state;
    this.setState({isSaving: true});
    try {
      await onSubmit({address, share});
      this.props.onHide();
    } catch (e) {
      this.setState({transactionError: e.message})
    }
    this.setState({isSaving: false});
  };

  renderAddressError = () => {
    const {address, isAddressErrorVisible} = this.state;
    const errorList =  validateAddress(address);
    if (!errorList.length || !isAddressErrorVisible) {
      return null;
    }

    return (
      <Message style={{
        paddingLeft: '14px',
        paddingTop: '7px',
        paddingBottom: '7px',
        marginTop: '4px',
        marginBottom: '10px'
      }} error list={errorList}/>
    );
  };

  renderTransactionError = () => {
    if (!this.state.transactionError) {
      return null
    }

    return (
      <div>
        <Divider/>
        <div style={{color: 'red'}}>
          {this.state.transactionError}
        </div>
      </div>
    )
  };

  render() {
    const {isVisible, onHide, editType} = this.props;
    const {isSaving, share, address} = this.state;
    const title = editType ? 'Edit Share Holder' : 'New Share Holder';
    const button = editType ?
      <Icon link name='edit' size='large' onClick={this.onShowHandle}/> :
      <Button fluid attached='top' onClick={this.onShowHandle}>Add Share</Button>;
    const submitDisabled = isSaving || !address || !share;

    return (
      <span>
        {button}
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>{title}</Modal.Header>
            <Modal.Content>
              <div>Address:</div>
              <Input fluid value={address} disabled={isSaving || editType} onChange={this.onAddressChange} onBlur={this.onAddressBlur}/>
              {this.renderAddressError()}
              <div>Share:</div>
              <Input type="number" min={0} step={1} fluid value={share} disabled={isSaving}
                     onChange={this.onShareChange}/>
              <Divider />
              <span>PS: Please be patient this may take a while...</span>
              {this.renderTransactionError()}
            </Modal.Content>
            <Modal.Actions>
              <Button negative disabled={isSaving} onClick={onHide}>Cancel</Button>
              <Button positive loading={isSaving} disabled={submitDisabled} icon='checkmark' labelPosition='right'
                      content='Save' onClick={this.onSubmit}/>
            </Modal.Actions>
        </Modal>
    </span>
    );
  }
}

export const ShareHolderSave = withVisible(ShareHolderSaveComponent);
