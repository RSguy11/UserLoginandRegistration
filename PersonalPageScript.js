
document.getElementById("addproject").addEventListener("click",CreateNewProject);
let i = 0;
let z = 0;

function testFunction() {
    document.getElementById("test1").innerText= i;
    i++;
}

    function CreateNewProject(){

        z++;
        //get the box holding all the project
        const div = document.getElementById("projectBoxes");

        //create new project with distinct name
        const project = document.createElement("div");
        project.setAttribute('id',('project'+z));
        project.classList = "project";
        
        //create new input fro title
        const innerTitle = document.createElement("input");
        innerTitle.placeholder ="Project Title";
        innerTitle.setAttribute('id',('projectTitle'+z));

        //create new input for discription
        const innerDiscription = document.createElement("input");
        innerDiscription.placeholder = "Project Discription";
        innerDiscription.setAttribute('id',('projectDis'+z));


        //create button to 
        const createbutton = document.createElement("button");
        createbutton.innerText = "Set";
        createbutton.setAttribute('id',('SetValues'+z));
        createbutton.addEventListener('click',SetProjectDetails);

        //add all inputs to the div 
        project.appendChild(innerTitle);
        project.appendChild(innerDiscription);
        project.appendChild(createbutton);

       
            // add the div to the porject box 
        div.appendChild(project);

    }





     function SetProjectDetails() { 
    
        //get the box holding all the varibles
        const div = document.getElementById("projectBoxes");

        //get the current project box
        const project = document.getElementById("project"+z);


        //get the current values of the input boxes
        var NewTitle = document.getElementById("projectTitle"+z).value;
        var NewDiscription = document.getElementById("projectDis"+z).value;


        //create link to projet page with input value as text
        const NewProjectTitle = document.createElement("a");
        NewProjectTitle.href = "/ProjectCreation.html";
        NewProjectTitle.innerText = NewTitle;

        //create discrption with value in input box
        const NewProjectDis = document.createElement("p");
        NewProjectDis.innerText = NewDiscription;

        //remvoing all past elements
         project.removeChild(document.getElementById("projectTitle"+z));
         project.removeChild(document.getElementById("projectDis"+z));
         project.removeChild(document.getElementById("SetValues"+z));



        //add to the current porject box
        project.appendChild(NewProjectTitle);
        project.appendChild(NewProjectDis); 
     }