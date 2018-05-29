import React, {Component} from 'react';
import {Button, Icon, Loader, Modal} from 'semantic-ui-react';
import {connect} from 'react-redux';
import * as actions from '../actions';
const ETHEREUM_NETWORK = process.env.REACT_APP_ETHEREUM_NETWORK;

class WaitingModal extends Component {
    render() {
        const { modalInfo, closeModal} = this.props;
        return (
            <Modal open={modalInfo.showModal} basic>
                <Modal.Content>
                    <div style={{height: '100px'}}>
                        <Loader
                            size="large"
                            disabled={!modalInfo.showLoader}
                        />
                    </div>
                    <div style={{fontSize: '20px', textAlign: 'center'}}>
                        {modalInfo.contract ? (
                            <div>
                                <a
                                    href={`https://${ETHEREUM_NETWORK}/address/${modalInfo.contract}`} target="_blank"
                                >
                                    Congratulations your scaffold is deployed.
                                    Visit this link to see it on-chain!
                                </a>
                                <Modal.Actions>
                                    <Button
                                        basic
                                        color="red"
                                        inverted
                                        onClick={() => {
                                            closeModal();
                                        }}
                                    >
                                        <Icon name="close"/> Close
                                    </Button>
                                </Modal.Actions>
                            </div>
                        ) : (
                            'Open is deploying your scaffold on-chain. Please be patient this may take a while...'
                        )}
                    </div>
                </Modal.Content>
            </Modal>
        );
    }
}

const mapStateToProps = state => {
    const {modalInfo} = state;
    return {modalInfo};
};

WaitingModal = connect(mapStateToProps, actions)(WaitingModal);

export default WaitingModal;
