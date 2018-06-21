import React from 'react';
import {withVisible} from '../components-ui/withVisible';
import {Button, Divider, Icon, Modal} from 'semantic-ui-react';

class RemoveDevShareComponent extends React.Component {

  state = {
    isSaving: false,
  };

  onSubmit = async e => {
    this.setState({isSaving: true});
    try {
      await this.props.onSubmit();
      this.props.onHide();
    } catch (e) {
      console.log(e);
    }
    this.setState({isSaving: false});
  };

  render() {
    const {isVisible, onShow, onHide} = this.props;
    const {isSaving} = this.state;

    return (
      <span>
        <Icon link name='remove' size='large' onClick={onShow}/>
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>Remove Share Holder</Modal.Header>
            <Modal.Content>
              You are removing Share Holder. Are you sure?
              <Divider />
              <span>PS: Please be patient this may take a while...</span>
            </Modal.Content>
            <Modal.Actions>
              <Button negative disabled={isSaving} onClick={onHide}>Cancel</Button>
              <Button positive loading={isSaving} disabled={isSaving} icon='checkmark' labelPosition='right'
                      content='Remove' onClick={this.onSubmit}/>
            </Modal.Actions>
          </Modal>
      </span>
    );
  }
}

export const RemoveDevShare = withVisible(RemoveDevShareComponent);
