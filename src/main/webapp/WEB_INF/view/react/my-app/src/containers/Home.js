import React from "react";
import SearchBox from "../components/SearchBox";
import Card from "../components/Card";
import CardList from "../components/CardList";
import TopBar from "../components/TopBar";

const DOMAIN = "http://localhost:8080";

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchField: '',
            items:[],
            message:'',
            showNext:false,
            prevCounter:0,
            sortOptions:[],
            filterOptions:[],
            onlyWomenShoes:false
        };
        this.onHandleSearch=this.onHandleSearch.bind(this);
    }

    componentDidMount() {
        this.fetchSortOptions();
        this.fetchFilterOptions();
    }

    fetchSortOptions = () => {
        fetch(DOMAIN+'/items/sorts/sortNames',{
            headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'}
        }).then((res)=>res.json()).then((objs)=> {
            this.setState({sortOptions:objs});
        })
    };

    fetchFilterOptions = () =>{
        fetch(DOMAIN+'/items/filters/filterNames',{
            headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'}
        }).then((res)=>res.json()).then((objs)=> {
            this.setState({filterOptions:objs});
        })
    };

    onSearchChange = (searchField) => {
        this.setState({searchField:searchField.target.value});
    };

    onHandleSearch = () => {
        let searchField = this.state.searchField;
        console.log(searchField);
        if(!searchField){
            this.setState({message:'Search term must have at least one character',items:[]});
        }
        //in case not empty
        else{
            this.setState({message:'Fetching data...'});
            fetch(DOMAIN+'/items/'+searchField,{
                headers: {
                    'Accept': 'application/json', 'Content-Type': 'application/json'}
            }).then((res)=>res.json()).then((objs)=> {
                if (!objs || objs.length === 0){
                    console.log('no objects were found');
                    this.setState({message:'Sorry... nothing was found'})
                }
                this.setState({items:objs, prevCounter:0});
                this.fetchShouldShowNext();
            }).catch = (e) =>{
                this.setState({message:'Oh ooh, something bad happened. Please try again later.'})
            };
        }
    };

    fetchShouldShowNext = () => {
        fetch(DOMAIN+'/items/offset/shouldPresentNext',{
            headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'}
        }).then((res)=>res.json()).then((boolean)=> {
            this.setState({showNext:boolean});
        });
    };

    fetchOffsets = (nextOrPrev) => {
        fetch(DOMAIN+'/items/offset/'+nextOrPrev,{
            headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'}
        }).then((res)=>res.json()).then((objs)=> {
            if (!objs || objs.length === 0){
                console.log('no objects were found');
                this.setState({message:'Sorry... nothing was found'})
            }
            this.setState({items:objs});
        }).then(()=>{this.fetchShouldShowNext()}).catch = (e) =>{
            this.setState({message:'Oh ooh, something bad happened. Please try again later.'})
        };
    };

    nextHandler = () => {
        this.fetchOffsets("next");
        this.setState({prevCounter:this.state.prevCounter+1})
    };

    prevHandler = () => {
        this.fetchOffsets("prev");
        this.setState({prevCounter:this.state.prevCounter-1});
    };

    sortHandler = (sortOption) => {
        fetch(DOMAIN+'/items/sortItems/'+sortOption,{
            headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'}
        }).then((res)=>res.json()).then((objs)=> {
            if (!objs || objs.length === 0){
                console.log('no objects were found');
                this.setState({message:'Sorry... nothing was found'})
            }
            this.setState({items:objs});
        }).catch = (e) =>{
            this.setState({message:'Oh ooh, something bad happened. Please try again later.'})
        };
    };

    filterHandler = (filter) => {
        fetch(DOMAIN+'/items/filterItems/'+filter,{
            headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'}
        }).then((res)=>res.json()).then((objs)=> {
            if (!objs || objs.length === 0){
                console.log('no objects were found');
                this.setState({message:'Sorry... nothing was found'})
            }
            this.setState({items:objs});
        }).catch = (e) =>{
            this.setState({message:'Oh ooh, something bad happened. Please try again later.'})
        };
    };

    womenShoesHandler = (isOnlyWomenShoes) => {
        this.setState({onlyWomenShoes:isOnlyWomenShoes});
        fetch(DOMAIN+'/items/onlyWomenShoes',{
            method:'post',headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }, body:JSON.stringify({
                isOnlyWomenShoes})
        }).then(()=>this.onHandleSearch()).catch(console.log);
    };

    render() {
        let items = this.state.items;
        let message = this.state.message;
        return (
            <div className='tc'>
                <div className='tc'>
                    <h1 className="f1"> Welcome to our online shoes store</h1>
                    <hr/>
                </div>
                <div align="center" style={{display:"flex", alignItems:"center",justifyContent: "center"}}>
                    <SearchBox searchChange={this.onSearchChange}/>
                    <input type="button" value="Search" style={{cursor:"pointer"}} onClick={this.onHandleSearch}/>
                </div>
                <div>
                    <TopBar items={items} message={message} nextHandler={this.nextHandler} prevHandler={this.prevHandler}
                            sortHandler={this.sortHandler} filterHandler={this.filterHandler} prevCounter={this.state.prevCounter}
                            showNext={this.state.showNext} sortOptions={this.state.sortOptions} filterOptions={this.state.filterOptions}
                            onlyWomenShoes={this.state.onlyWomenShoes} womenShoesHandler={this.womenShoesHandler}
                    />
                </div>
                <div style={{display:"flex",alignItems:"center",justifyContent: "center"}}>
                    <CardList items={items}/>
                </div>
            </div>

        );
    }
}

export default Home;