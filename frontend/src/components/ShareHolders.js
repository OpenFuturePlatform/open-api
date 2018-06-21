import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import '../css/table.css';
import {EtherscanLink} from '../components-ui/EtherscanLink';
import {Segment} from 'semantic-ui-react';
import {addShareHolder, editShareHolder, fetchShareHolders, removeShareHolder} from '../actions/dev-shares';
import {AddEditShareHolder} from './AddEditShareHolder';
import {RemoveDevShare} from './RemoveDevShare';

const getColumns = (scaffold, onEdit, onRemove) => [
  {
    Header: 'Share Holder Address',
    accessor: 'address',
    Cell: ({value}) => <EtherscanLink>{value}</EtherscanLink>,
    sortable: false
  },
  {
    Header: 'Holder Share',
    accessor: 'share',
    width: 200,
    Cell: ({value}) => <span>{value}%</span>,
    sortable: false
  },
  {
    Header: '',
    accessor: 'address',
    width: 150,
    Cell: ({value, original}) => (
      <span>
        <AddEditShareHolder editType devShare={original} onSubmit={(devShare) => onEdit(devShare)} />{' '}
        <RemoveDevShare onSubmit={() => onRemove(value)} />
      </span>
    ),
    sortable: false,
  }
];

export class DevSharesComponent extends Component {

  componentDidMount() {
    const {scaffold} = this.props;
    this.props.actions.fetchShareHolders(scaffold);
  }

  onAddShareHolder = (shareHolder) => {
    const {scaffold} = this.props;
    return this.props.actions.addShareHolder(scaffold, shareHolder);
  };

  onEditShareHolder = (shareHolder) => {
    const {scaffold} = this.props;
    return this.props.actions.editShareHolder(scaffold, shareHolder);
  };

  onRemoveShareHolder = (holderAddress) => {
    const {scaffold} = this.props;
    return this.props.actions.removeShareHolder(scaffold, holderAddress);
  };

  render() {
    const {devShares, scaffold} = this.props;

    return (
      <div className="table-with-add">
        <AddEditShareHolder onSubmit={this.onAddShareHolder}/>
        <Segment attached styles={{padding: 0}}>
          <ReactTable data={devShares} columns={getColumns(scaffold, this.onEditShareHolder, this.onRemoveShareHolder)}
                      className="-striped" showPagination={false} resizable={false} minRows={1}
                      pageSize={devShares.length}
          />
        </Segment>
      </div>
    );
  }
}

const mapStateToProps = ({devShares}, {scaffold}) => ({scaffold, devShares});

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({
    fetchShareHolders,
    addShareHolder,
    editShareHolder,
    removeShareHolder
  }, dispatch)
});

export const ShareHolders = connect(mapStateToProps, mapDispatchToProps)(DevSharesComponent);
