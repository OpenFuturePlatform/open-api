import React from 'react';
import {withVisible} from '../components-ui/withVisible';
import {Button, Divider, Icon, Input, Modal} from 'semantic-ui-react';

class AddEditShareHolderComponent extends React.Component {

  constructor(props) {
    super(props);

    const address = props.editType ? props.devShare.address : '';
    const share = props.editType ? props.devShare.share : '';

    this.state = {
      address,
      share,
      addressError: '',
      shareError: '',
      isAddressErrorVisible: false,
      isShareErrorVisible: false,
      isSaving: false,
    };
  }

  onAddressChange = e => this.setState({address: e.target.value});
  onShareChange = e => this.setState({share: Math.abs(e.target.value)});

  onSubmit = async e => {
    const {onSubmit} = this.props;
    const {address, share} = this.state;
    this.setState({isSaving: true});
    try {
      await onSubmit({address, share});
      this.props.onHide();
    } catch (e) {
      console.log(e);
    }
    this.setState({isSaving: false});
  };

  render() {
    const {isVisible, onShow, onHide, editType} = this.props;
    const {isSaving, share, address} = this.state;
    const title = editType ? 'Edit Share Holder' : 'New Share Holder';
    const button = editType ?
      <Icon link name='edit' size='large' onClick={onShow}/> :
      <Button fluid attached='top' onClick={onShow}>Add Share</Button>;

    return (
      <span>
        {button}
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>{title}</Modal.Header>
            <Modal.Content>
              <div>Address:</div>
              <Input fluid value={address} disabled={isSaving || editType} onChange={this.onAddressChange}/>
              <div>Share:</div>
              <Input type="number" min={0} step={1} fluid value={share} disabled={isSaving}
                     onChange={this.onShareChange}/>
              <Divider />
              <span>PS: Please be patient this may take a while...</span>
            </Modal.Content>
            <Modal.Actions>
              <Button negative disabled={isSaving} onClick={onHide}>Cancel</Button>
              <Button positive loading={isSaving} disabled={isSaving} icon='checkmark' labelPosition='right'
                      content='Save' onClick={this.onSubmit}/>
            </Modal.Actions>
        </Modal>
    </span>
    );
  }
}

export const AddEditShareHolder = withVisible(AddEditShareHolderComponent);
