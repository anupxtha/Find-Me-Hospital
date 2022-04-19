//const search = () => {
//  let query = $("#service").val();
//
//    if(query == ""){
//        $(".search-result").hide();
//    }else{
//
//        let url = `http://localhost:8181/search/${query}`;
//        fetch(url).then((response)=>{
//            return response.json();
//        }).then((data)=>{
//            // console.log(data);
//            let text = `<div class='list-group'>`;
//
//            data.forEach((service)=>{
//               text += `<option value="${service.id}"> ${service.s_name}</option>`;
//            });
//
//            text += `</div>`;
//            $(".search-result").html(text);
//            $(".search-result").show();
//        })
//
//    }
//
//}

// <select className='list-group-item list-group-action'> </select>





